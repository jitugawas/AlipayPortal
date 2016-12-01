package com.mkyong.common;

import java.util.Calendar;
import java.util.Timer;

import javax.annotation.PostConstruct;

import com.payitnz.scheduler.ScheduledReconcileTask;
import com.payitnz.scheduler.ScheduledSettlementTask;

/**
 *
 * @author 
 */

//Main class
public class SchedulerMain {
	public static void main(String args[]) throws InterruptedException {

		Timer time = new Timer(); // Instantiate Timer Object
		ScheduledReconcileTask reconcileTask = new ScheduledReconcileTask(); // Instantiate SheduledTask class
		time.schedule(reconcileTask, 0, 60*60*1000); // Create Repetitively task for every 1 hr
		
		ScheduledSettlementTask settlementTask = new ScheduledSettlementTask(); // Instantiate SheduledTask class		
		time.schedule(settlementTask, 15*60*1000, 60*60*1000); // Create Repetitively task for every 1 hr
		
	}
	
	@PostConstruct
	public void startScheduledTask() throws InterruptedException{
		
		System.out.println("Before start of thread");
		Timer time = new Timer(); // Instantiate Timer Object
				
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 19);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		
		//Calendar firstTaskTime = getFirstTime();
		 
		ScheduledReconcileTask reconcileTask = new ScheduledReconcileTask(); // Instantiate SheduledTask class
		time.schedule(reconcileTask, 0, 30*60*1000); // Create Repetitively task for every 30 mins
		
		System.out.println("After start of thread");	
		
		ScheduledSettlementTask settlementTask = new ScheduledSettlementTask(); // Instantiate SheduledTask class		
		time.schedule(settlementTask, 15*60*1000, 30*60*1000); // Create Repetitively task for every 30 mins
		
	}
	
}