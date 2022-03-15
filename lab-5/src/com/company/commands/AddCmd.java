package com.company.commands;

import com.company.collection.MusicBand;
import com.company.collection.MusicBandStorage;
import com.company.container.Message;
import com.company.util.IOStream;

public class AddCmd extends Command {
   public AddCmd(MusicBandStorage target, String cmdName, IOStream ioStream) {
      super(target, cmdName, ioStream,
              "Добавить элемент в коллекцию"
      );
   }

   @Override
   public Message exec(Message message){
      //TODO: do some shit
      ioStream.writelnterm("ДОБАВЛЕНИЕ НОВОГО ЭЛЕМЕНТА В КОЛЛЕКЦИЮ " + target.getName());
      ioStream.writelnterm("Введите значения следующих свойств: ");
      MusicBand musicBand = ioStream.readMusicBand();
      target.add(musicBand);
      ioStream.writelnterm("(Новый элемент добавлен)");


      return message;
   }

}
