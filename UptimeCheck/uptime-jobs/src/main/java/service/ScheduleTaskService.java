package service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleTaskService {

	@Autowired
	WebsiteStatusService websiteStatusService;
	@Autowired
	TaskScheduler scheduler;
	Map<String, WebsiteMonitor> jobsMap = new HashMap<>();

	public void scheduleMonitoring(WebsiteRegister wr){
		if(wr.isActive()){
			if(!jobsMap.containsKey(wr.getName())){
				// get the lock from Distribute lock server
				// if lock obtained then add tasks to scheduler
				// else skip - i.e some other server is working on it
				addTaskToScheduler(wr);
			}else{
				WebsiteRegister wrPresent = jobsMap.get(wr.getName()).getWebsiteRegister();
				if(!wrPresent.getIntervalType().equals(wr.getIntervalType()) ||
						wrPresent.getInterval()!=wr.getInterval() ||
						!wrPresent.getUrl().equals(wr.getUrl())){
					log.info("Register modified, updating monitoring task");
					removeTaskFromScheduler(wr.getName());
					addTaskToScheduler(wr);
				}
			}
		}
		if(!wr.isActive() && jobsMap.containsKey(wr.getName())){
			removeTaskFromScheduler(wr.getName());
		}
	}
	
	private void addTaskToScheduler(WebsiteRegister wr) {
		log.info("Adding New Task for monitoring;wr;{}", wr);
		ScheduledFuture<?> scheduledTask = scheduler.schedule(getRunnable(wr), getPeriodicTrigger(wr));
		WebsiteMonitor wm = new WebsiteMonitor(wr,scheduledTask);
		jobsMap.put(wr.getName(), wm);
	}

	private void removeTaskFromScheduler(String name) {
		if(jobsMap.containsKey(name)){
			log.info("Removing Task from monitoring;name;{}", name);
			WebsiteMonitor wm = jobsMap.remove(name);
			ScheduledFuture<?> scheduledTask = wm.getScheduledFuture();
			scheduledTask.cancel(true);
			Optional<WebsiteStatus> wsCache = websiteStatusService.findInCache(name);
			if(wsCache.isPresent()){
				wsCache.get().setMonitoringActive(false);
				save(wsCache.get());
			}
		}
	}

	private Runnable getRunnable(WebsiteRegister wr){
		return new Runnable() {
			@Override
			public void run() {
				WebsiteStatus ws = getCurrentStatus(wr);
				Optional<WebsiteStatus> wsCache = websiteStatusService.findInCache(wr.getName());
				if(wsCache.isPresent() && wsCache.get().getWebsiteStatusType().equals(ws.getWebsiteStatusType())){
					ws.setSince(wsCache.get().getSince());
					ws.setStatusCount(wsCache.get().getStatusCount()+1);
					ws.setAverageResponseTime((ws.getAverageResponseTime()+wsCache.get().getAverageResponseTime())/ws.getStatusCount());
				}
				log.info("Updating status;{}",wr.getName());
				save(ws);
				if(TypeWebsiteStatus.DOWN.equals(ws.getWebsiteStatusType()) && ws.getStatusCount()%3==0){
					log.info("Notifying Notification Server to send email for failed attempts.....");
				}
			}
		};
	}

	private WebsiteStatus getCurrentStatus(WebsiteRegister wr){
		long startTime = System.currentTimeMillis();
		boolean isAccessible = isAccessable(wr.getUrl());
		long endTime = System.currentTimeMillis();
		return WebsiteStatus.builder()
				.name(wr.getName())
				.url(wr.getUrl())
				.websiteStatusType(isAccessible? TypeWebsiteStatus.UP:TypeWebsiteStatus.DOWN)
				.since(new Date(startTime))
				.averageResponseTime(endTime-startTime)
				.statusCount(1)
				.isMonitoringActive(true)
				.build();
	}

	private PeriodicTrigger getPeriodicTrigger(WebsiteRegister websiteRegister){
		return new PeriodicTrigger(
				websiteRegister.getInterval(),
				websiteRegister.getIntervalType().equals(TypeInterval.HOURS) ? TimeUnit.HOURS : TimeUnit.MINUTES);
	}

	private boolean isAccessable(String url) {
		url = url.replaceFirst("https", "http");
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			if (connection.getResponseCode() != 200) {
				return false;
			}
		} catch (IOException exception) {
			log.info("Exception ; {}", exception.getMessage());
			return false;
		}
		return true;
	}

	private void save(WebsiteStatus ws){
		websiteStatusService.updateCache(ws);
		websiteStatusService.updateDB(ws);
	}

}