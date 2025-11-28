/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.dao;

import com.musik.studio.model.Studio;
import org.hibernate.Session;
import java.util.List;

public class StudioDAO extends GenericDAO<Studio> {
    
    public StudioDAO() {
        super(Studio.class);
    }
    
    public List<Studio> findActiveStudios() {
        try (Session session = sessionFactory.openSession()) {
            List<Studio> studios = session.createQuery(
                "FROM Studio WHERE active = true ORDER BY name", Studio.class).list();
            System.out.println("🔍 Found " + studios.size() + " active studios");
            for (Studio studio : studios) {
                System.out.println("   - " + studio.getName() + " (ID: " + studio.getId() + ")");
            }
            return studios;
        } catch (Exception e) {
            System.out.println("❌ Error in findActiveStudios: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list instead of null
        }
    }
}
