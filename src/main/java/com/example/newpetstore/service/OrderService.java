package com.example.newpetstore.service;

import com.example.newpetstore.entity.Order;
import com.example.newpetstore.entity.Pet;
import com.example.newpetstore.entity.User;
import com.example.newpetstore.repository.OrderRepository;
import com.example.newpetstore.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PetRepository petRepository;
    public Order save(Order order){
        return orderRepository.save(order);
    }

//    public Optional<Order> findById(int id){
//        Optional<Order> byId = orderRepository.findById(id);
//
//        if (byId.isPresent()){
//
//            if (byId.get().getUser().getId().equals(user.getId())) {
//
//                return byId;
//            }
//        }
//        return Optional.empty();
//    }
//
//    public boolean delete(int id){
//        Optional<Order> byId = orderRepository.findById(id);
//
//        if (byId.isPresent()){
//
//            Order order = byId.get();
//
//            if (order.getUser().getUsername().equals(username)){
//
//                petRepository.deleteById(id);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean addPet(int orderId, int petId){
//
//        Optional<Order> orderById = orderRepository.findById(orderId);
//        Optional<Pet> petById = petRepository.findById(petId);
//
//
//        if (orderById.isPresent() & petById.isPresent()){
//
//            Order order = orderById.get();
//
//            if (order.getUser().getUsername().equals(username)) {
//
//                Pet pet = petById.get();
//                List<Pet> pets = order.getPet();
//                pets.add(pet);
//                order.setPet(pets);
//                orderRepository.save(order);
//                return true;
//            }
//        }
//        return false;
//    }
}