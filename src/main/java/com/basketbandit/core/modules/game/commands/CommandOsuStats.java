package com.basketbandit.core.modules.game.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.game.osu.User;
import com.basketbandit.core.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.xhtmlrenderer.swing.Java2DRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class CommandOsuStats extends Command {

    public CommandOsuStats() {
        super("osu", "com.basketbandit.core.modules.game.ModuleGame", new String[]{"-osu [user]", "-osu [user] [game]"}, null);
    }

    public CommandOsuStats(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 2);

            String username = commandParameters[0];
            int mode;
            Long unique = System.nanoTime();

            if(commandParameters.length > 1) {
                mode = Integer.parseInt(commandParameters[1]);
            } else {
                mode = 0;
            }

            String modeString;
            switch(mode) {
                case 0: modeString = "osu!";
                    break;
                case 1: modeString = "taiko";
                    break;
                case 2: modeString = "catch the neat";
                    break;
                case 3: modeString = "osu!mania";
                    break;
                default: modeString = "unknown";
            }

            // Buffers JSON from the given URL and the uses ObjectMapper to turn it into usable Java objects.
            String json = Utils.bufferJson("https://osu.ppy.sh/api/get_user?k=" + Configuration.OSU_API + "&u=" + username + "&m=" + mode);

            // Jackson expects an array when Json objects start with [, so this removes it.
            json = json.substring(1,json.length()-1);

            if(json.equals("")) {
                Utils.sendMessage(e,"Sorry " + e.getAuthor().getAsMention() + ", user " + username + " was not found.");
                return;
            }

            User user = new ObjectMapper().readValue(json, new TypeReference<User>(){});

            // We pull the accuracy out to get a quick and easy 2dp string. (including decimal point)
            String accuracy = Double.parseDouble(user.getAccuracy())+"";
            accuracy = accuracy.substring(0,4);

            // xHTML code dynamically written to a file (since Java2DRenderer doesn't take a string?)
            String html = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'><html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'><head> <title>osu</title> <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/> <style type='text/css'> html, body{width: 425px; height: 200px; font-family: Helvetica Neue,Helvetica,Arial,sans-serif; position: relative; padding: 0px; border: 0px; margin: 0px;}#backgroundContainer{width: 425px; height: 200px; background-image: url(/root/htmlresources/osu/page-dark@2x.png); border-radius: 10px; z-index: 0;}#container{width: 425px; height: 200px; background-color: rgba(20,20,20,0.7); color: white; border-radius: 10px; z-index: 10;}#name{font-size: 3em; padding-left: 15px; padding-top: 5px;}#rank{position: absolute; top: 70px; left: 320px; font-size: 13px;}#rank2{position: absolute; top: 90px; left: 320px; font-size: 13px;}#level{position: absolute; top: 70px; left: 20px;}#playcount{position: absolute; width: 100px; top: 65px; left: 60px; line-height: 20px; text-align: center; font-size: 12px;}#accuracy{position: absolute; width: 100px; top: 65px; left: 145px; line-height: 20px; text-align: center; font-size: 12px;}#playtime{position: absolute; width: 100px; top: 65px; left: 225px; line-height: 20px; text-align: center; font-size: 12px;}.stat{font-size: 20px;}.countryFlag{height: 13px; border-radius: 2px;}#levelBadge{position: absolute; width: 40px; left: 20px; top: 68px;}#levelText{position: absolute; width: 100px; text-align: center; left: -10px; top: 73px; font-size: 14px;}#scoreRank img, #scoreRank div{position: absolute; width: 60px; text-align: center;}#ssp{top: 110px; left: 20px;}#ss{top: 110px; left: 102px;}#sp{top: 110px; left: 184px;}#s{top: 110px; left: 266px;}#a{top: 110px; left: 348px;}#sspt{top: 160px; left: 20px;}#sst{top: 160px; left: 102px;}#spt{top: 160px; left: 187px;}#st{top: 160px; left: 266px;}#at{top: 160px; left: 348px;}</style></head><body> <div id='backgroundContainer'> <div id='container'> <div id='name'>"+user.getUsername()+"</div><div id='rank'><img class='countryFlag' src='/root/htmlresources/osu/flags/ALL.png' alt=''/> #"+user.getPpRank()+"</div><div id='rank2'><img class='countryFlag' src='/root/htmlresources/osu/flags/"+user.getCountry()+".png' alt=''/> #"+user.getPpCountryRank()+"</div><img id='levelBadge' src='/root/htmlresources/osu/levelbadge@2x.png' alt=''/> <div id='levelText'>"+(int) Double.parseDouble(user.getLevel())+"</div><div id='playcount'>PLAYCOUNT<br/><span class='stat'>"+user.getPlaycount()+"</span></div><div id='accuracy'>ACCURACY<br/><span class='stat'>"+accuracy+"%</span></div><div id='playtime'>PLAYTIME<br/><span class='stat'>"+((Integer.parseInt(user.getTotalSecondsPlayed())/60)/60)+"H</span></div><div id='scoreRank'> <img id='ssp' src='/root/htmlresources/osu/Score-SSPlus-Small-60@2x.png' alt=''/> <img id='ss' src='/root/htmlresources/osu/Score-SS-Small-60@2x.png' alt=''/> <img id='sp' src='/root/htmlresources/osu/Score-SPlus-Small-60@2x.png' alt=''/> <img id='s' src='/root/htmlresources/osu/Score-S-Small-60@2x.png' alt=''/> <img id='a' src='/root/htmlresources/osu/Score-A-Small-60@2x.png' alt=''/> <div id='sspt'>"+user.getCountRankSsh()+"</div><div id='sst'>"+user.getCountRankSs()+"</div><div id='spt'>"+user.getCountRankSh()+"</div><div id='st'>"+user.getCountRankS()+"</div><div id='at'>"+user.getCountRankA()+"</div></div></div></div></body></html>";
            BufferedWriter writer = new BufferedWriter(new FileWriter("htmlresources/osu/imagecache/"+unique+".xhtml"));
            writer.write(html);
            writer.close();



            // Reads xHTML code from file, renders, buffers an image and then writes that to a file.
            // Subclasses 2DRenderer to allow transparent images and such.
            final java.awt.Color TRANSPARENT = new Color(255, 255, 255, 0);
            final Java2DRenderer renderer = new Java2DRenderer(new File("htmlresources/osu/imagecache/"+unique+".xhtml"), 425, 200) {
                @Override
                protected BufferedImage createBufferedImage(final int width, final int height) {
                    final BufferedImage image = org.xhtmlrenderer.util.ImageUtil.createCompatibleBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    org.xhtmlrenderer.util.ImageUtil.clearImage(image, TRANSPARENT);
                    return image;
                }
            };
            BufferedImage img = renderer.getImage();
            ImageIO.write(img, "png", new File("htmlresources/osu/imagecache/"+unique+".png"));

            Utils.sendMessage(e,"Displaying stats for " + user.getUsername() + " on " + modeString);
            Utils.sendMessage(e, new File("htmlresources/osu/imagecache/"+unique+".png"));

        } catch(Exception ex) {
            ex.printStackTrace();
            Utils.sendMessage(e, "There was an issue processing the request for command: " + e.getMessage().getContentDisplay());
        }
    }
}
