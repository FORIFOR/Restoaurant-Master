package com.example.restomaster.domain.model

/**
 * Represents the operating mode of the device
 */
enum class AppMode {
    /**
     * Host device - has DB and local HTTP server, accepts orders from clients and customers
     */
    HOST,
    
    /**
     * Client device - connects to host device for order management and checkout
     */
    CLIENT
}
