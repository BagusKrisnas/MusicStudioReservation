/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    static {
        try {
            // Create registry
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
            
            // Create MetadataSources
            MetadataSources sources = new MetadataSources(registry);
            
            // Add annotated classes
            sources.addAnnotatedClass(com.musik.studio.model.Studio.class);
            sources.addAnnotatedClass(com.musik.studio.model.Customer.class);
            sources.addAnnotatedClass(com.musik.studio.model.Reservation.class);
            sources.addAnnotatedClass(com.musik.studio.model.Payment.class);
            
            // Create Metadata
            Metadata metadata = sources.getMetadataBuilder().build();
            
            // Create SessionFactory
            sessionFactory = metadata.getSessionFactoryBuilder().build();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
