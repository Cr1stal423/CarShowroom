package com.dealership.car.constants;

import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Constants {
    public static final String USER_ROLE = "USER";
    public static final String OPERATOR_ROLE = "OPERATOR";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String OWNER_ROLE = "OWNER";
    public static final String NEW_USER = "New user";
    public static final String USER_EXIST = "User exist";
    public static final String CLOSE_STATUS = "Message closed";

    public static final List<Product.AvailabilityStatus> AVAILABILITY_STATUSES = Arrays.asList(
            Product.AvailabilityStatus.AVAILABLE,
            Product.AvailabilityStatus.NOT_AVAILABLE,
            Product.AvailabilityStatus.COMING_SOON
    );
    public static final  List<TechnicalData.BodyType> BODY_TYPES = Arrays.asList(
            TechnicalData.BodyType.COUPE,
            TechnicalData.BodyType.CONVERTIBLE,
            TechnicalData.BodyType.HATCHBACK,
            TechnicalData.BodyType.SEDAN,
            TechnicalData.BodyType.SUV,
            TechnicalData.BodyType.TRUCK,
            TechnicalData.BodyType.WAGON,
            TechnicalData.BodyType.VAN
    );
    public static  final List<TechnicalData.EngineType> ENGINE_TYPES = Arrays.asList(
            TechnicalData.EngineType.GASOLINE,
            TechnicalData.EngineType.DIESEL,
            TechnicalData.EngineType.ELECTRIC,
            TechnicalData.EngineType.HYBRID,
            TechnicalData.EngineType.HYDROGEN
    );
    public static final  List<TechnicalData.EnginePlacement> ENGINE_PLACEMENTS = Arrays.asList(
            TechnicalData.EnginePlacement.FRONT,
            TechnicalData.EnginePlacement.REAR,
            TechnicalData.EnginePlacement.MID
    );
    public static final List<OrderEntity.PaymentType> PAYMENT_TYPES = Arrays.asList(
            OrderEntity.PaymentType.BUY,
            OrderEntity.PaymentType.CREDIT
    );
    public static final List<OrderEntity.PaymentMethod> PAYMENT_METHODS = Arrays.asList(
            OrderEntity.PaymentMethod.CARD,
            OrderEntity.PaymentMethod.CASH
    );
}
