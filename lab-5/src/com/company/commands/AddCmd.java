package com.company.commands;

import com.company.collection.MusicBandStorage;
import com.company.container.Message;
import com.company.util.IOStream;

public class Add extends Command {


   public Add(MusicBandStorage target, String cmdName, IOStream ioStream) {
      super(target, cmdName, ioStream,
              "Добавить элемент в коллекцию"
      );
   }

   @Override
   public Message exec(Message message){
      //some shit
      return message;
   }

}
