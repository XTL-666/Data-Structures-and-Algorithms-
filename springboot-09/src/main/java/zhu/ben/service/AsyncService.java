package zhu.ben.service;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {


    @Async
    public void Hello() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("data is processing.......");
    }
}
