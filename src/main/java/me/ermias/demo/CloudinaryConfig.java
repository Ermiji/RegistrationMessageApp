package me.ermias.demo;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryConfig {

    private Cloudinary cloudinary;


    public CloudinaryConfig (@Value("${958845873577856}") String key,
                             @Value("${A_aRnNlvZ5OblzFtKdppplYS66Y}") String secret,
                             @Value("${dguorlfcw}") String cloud){


        cloudinary= Singleton.getCloudinary();
        cloudinary.config.cloudName=cloud;
        cloudinary.config.apiSecret=secret;
        cloudinary.config.apiKey=key;
    }

    public Map upload(Object file, Map options){

        try{
            return cloudinary.uploader().upload(file, options);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public String createUrl(String name, int width, int height, String action){


        return cloudinary.url().transformation(new Transformation().width(width).height(height).border("2px_solid_blue").crop(action)).imageTag(name);
    }

}
