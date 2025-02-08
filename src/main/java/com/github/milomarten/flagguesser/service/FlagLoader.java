package com.github.milomarten.flagguesser.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.milomarten.flagguesser.model.Flag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlagLoader {
    private final ObjectMapper objectMapper;

    private final Map<String, Flag> flags = new HashMap<>();

    @PostConstruct
    public void load() throws IOException {
        var resource = new ClassPathResource("flags.json");
        var flags = objectMapper.readValue(resource.getInputStream(), Flag[].class);

        Arrays.stream(flags)
                .forEach(flag -> this.flags.put(flag.getCountryCode(), flag));

        log.info("Loaded {} flags", flags.length);
        log.info("{}", this.flags);
    }

    public Optional<Flag> getFlag(String code) {
        return Optional.ofNullable(flags.get(code));
    }

    public List<Flag> flags() {
        return new ArrayList<>(flags.values());
    }
}
