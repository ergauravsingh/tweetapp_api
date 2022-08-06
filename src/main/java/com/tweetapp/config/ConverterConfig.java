package com.tweetapp.config;

import java.rmi.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class ConverterConfig  {

    @Autowired
    MongoDbFactory mongoDbFactory;


    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongoDbFactory), new MongoMappingContext());
        converter.setCustomConversions(customConversions());
        converter.afterPropertiesSet();
        return new MongoTemplate(mongoDbFactory, converter);
    }


    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(TimestampToDateConverter.INSTANCE);
        converters.add(DateToTimestampConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }
    
    enum TimestampToDateConverter implements Converter<Timestamp, Date>{
    	INSTANCE;
    	
    	 @Override
         public Date convert(Timestamp source) {
             return new Date(source.getTime());
         }
    }
    
    enum DateToTimestampConverter implements Converter<Date, Timestamp>{
    	INSTANCE;

		@Override
		public Timestamp convert(Date source) {
			return new Timestamp(source.getTime());
		}
    }

   
}