/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio;

import com.musik.studio.config.HibernateUtil;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("🔧 Testing Hibernate Configuration...");
            
            // Test Hibernate connection
            var sessionFactory = HibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
            
            System.out.println("✅ Hibernate SessionFactory created successfully!");
            System.out.println("✅ Database connection SUCCESS!");
            
            // Test simple query
            var transaction = session.beginTransaction();
            var result = session.createNativeQuery("SELECT 1 FROM DUAL").getResultList();
            transaction.commit();
            
            System.out.println("✅ Database query test SUCCESS!");
            
            session.close();
            System.out.println("🎉 All tests passed! Hibernate is ready to use.");
            
        } catch (Exception e) {
            System.out.println("❌ Hibernate connection FAILED!");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            System.out.println("🔚 Hibernate shutdown completed.");
        }
    }
}
