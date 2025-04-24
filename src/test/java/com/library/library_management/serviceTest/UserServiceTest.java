package com.library.library_management.serviceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ExecutorService executorService;

    @Test
    public void testConcurrentLoginsWithCompletableFuture(){
        int numOfUsers = 100;
        List<CompletableFuture<String>> futureList = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= numOfUsers; i++){
            final int userId = i;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                String username = "testUser"+userId;
                boolean result = userService.login(username, "testPassword");

                return username + " login " + (result ? "✅ SUCCESS" : "❌ FAILED");
            }, executorService);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("\n=== Login Result ===");
        for (CompletableFuture<String> future : futureList) {
            try {
                System.out.println(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n Total time Taken for " + numOfUsers + " logins: " + totalTime + " ms");

        executorService.shutdown();
    }
}
