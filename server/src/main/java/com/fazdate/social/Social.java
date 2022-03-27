package com.fazdate.social;

import com.fazdate.social.helpers.DatabaseRecreater;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Social implements CommandLineRunner {
    private final DatabaseRecreater databaseRecreater;

    @Override
    public void run(String... args) throws Exception {
        //databaseRecreater.recreateDatabase(10);
    }

}
