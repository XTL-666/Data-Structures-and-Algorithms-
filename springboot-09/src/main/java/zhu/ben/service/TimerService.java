package zhu.ben.service;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TimerService {

    @Scheduled(cron = "0/1 * * * * ?")
    public void hello(){
        System.out.println("the mail is sending to 计胖");
    }

}
