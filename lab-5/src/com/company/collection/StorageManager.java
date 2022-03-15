package com.company.collection;

import com.company.commands.Command;
import com.company.container.Message;
import com.company.container.Request;
import com.company.util.IOStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class StorageManager {
    private final MusicBandStorage storage;
    private Queue<Command> history;

    private final Map<String, Command> commands = new HashMap<>();

    public StorageManager(MusicBandStorage storage,
                          IOStream ioStream,
                          Command... commands) {
        this.storage = storage;
        for (Command command: commands){
            this.commands.put(command.getCmdName(), command);
        }

        //this.history
        //TODO: fill storage from file.json
    }

    boolean validateCmd(String cmd) {
        return commands.containsKey(cmd);
    }

    void executeCmd(Request request){
        commands.get(request.getCmdName()).exec(request);
    }


}
