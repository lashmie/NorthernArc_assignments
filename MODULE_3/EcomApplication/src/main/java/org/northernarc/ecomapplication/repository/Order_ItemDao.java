package org.northernarc.ecomapplication.repository;

import org.northernarc.ecomapplication.model.Order_Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Order_ItemDao extends JpaRepository<Order_Item,Long> {

}
