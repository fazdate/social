package com.fazdate.social;

import com.fazdate.social.helpers.DatabaseRecreater;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Social implements CommandLineRunner {
    private final DatabaseRecreater databaseRecreater;

    /**
     * This will delete every user and their data.
     * Only uncomment it if you need to do that!
     */
    @Override
    public void run(String... args) {

        // databaseRecreater.recreateDatabase(10);
    }

}
