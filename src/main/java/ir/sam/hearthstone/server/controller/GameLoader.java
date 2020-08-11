package ir.sam.hearthstone.server.controller;

import ir.sam.hearthstone.server.controller.logic.game.api.OnlineGameBuilderGenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class GameLoader {
     public void loadGame(String gameName,String jarUrl,String className,GameLobby gameLobby) throws
             MalformedURLException, ClassNotFoundException, NoSuchMethodException,
             InvocationTargetException, IllegalAccessException {
         URL url = new URL(jarUrl);
         URL[] urls = new URL[]{url};
         ClassLoader loader = new URLClassLoader(urls);
         Class<?> target = loader.loadClass(className);
         Method method = target.getDeclaredMethod("getGenerator");
         method.setAccessible(true);
         OnlineGameBuilderGenerator generator = (OnlineGameBuilderGenerator) method.invoke(null);
         gameLobby.addGame(gameName,generator);
     }
}
