/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.dao;

import com.musik.studio.model.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class CustomerDAO extends GenericDAO<Customer> {
    
    public CustomerDAO() {
        super(Customer.class);
    }
    
    public Customer findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Customer customer = session.createQuery(
                "FROM Customer WHERE email = :email", Customer.class)
                .setParameter("email", email)
                .uniqueResult();
            transaction.commit();
            return customer;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
    
    public List<Customer> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Customer> customers = session.createQuery(
                "FROM Customer WHERE name LIKE :name ORDER BY name", Customer.class)
                .setParameter("name", "%" + name + "%")
                .list();
            transaction.commit();
            return customers;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
