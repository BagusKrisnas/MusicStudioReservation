/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.dao;

import com.musik.studio.model.Reservation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationDAO extends GenericDAO<Reservation> {
    
    public ReservationDAO() {
        super(Reservation.class);
    }
    
    public List<Reservation> findByDateRange(LocalDateTime start, LocalDateTime end) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Reservation> reservations = session.createQuery(
                "FROM Reservation r WHERE r.startTime BETWEEN :start AND :end ORDER BY r.startTime", 
                Reservation.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .list();
            transaction.commit();
            return reservations;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
    
    public List<Reservation> findByStudioAndDate(Long studioId, LocalDateTime date) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
            
            List<Reservation> reservations = session.createQuery(
                "FROM Reservation r WHERE r.studio.id = :studioId AND " +
                "r.startTime BETWEEN :start AND :end ORDER BY r.startTime", 
                Reservation.class)
                .setParameter("studioId", studioId)
                .setParameter("start", startOfDay)
                .setParameter("end", endOfDay)
                .list();
            transaction.commit();
            return reservations;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
    
    public List<Reservation> findByCustomer(Long customerId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Reservation> reservations = session.createQuery(
                "FROM Reservation r WHERE r.customer.id = :customerId ORDER BY r.startTime DESC", 
                Reservation.class)
                .setParameter("customerId", customerId)
                .list();
            transaction.commit();
            return reservations;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
    
    public boolean isStudioAvailable(Long studioId, LocalDateTime startTime, LocalDateTime endTime, Long excludeReservationId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            String hql = "SELECT COUNT(r) FROM Reservation r WHERE " +
                        "r.studio.id = :studioId AND " +
                        "r.status IN ('PENDING', 'CONFIRMED') AND " +
                        "((r.startTime < :endTime AND r.endTime > :startTime))";
            
            if (excludeReservationId != null) {
                hql += " AND r.id != :excludeId";
            }
            
            var query = session.createQuery(hql, Long.class)
                .setParameter("studioId", studioId)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime);
            
            if (excludeReservationId != null) {
                query.setParameter("excludeId", excludeReservationId);
            }
            
            Long count = query.uniqueResult();
            transaction.commit();
            return count == 0;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
