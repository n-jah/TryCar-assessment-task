package com.example.trycar_assessment_task.util

import org.junit.Assert.*
import org.junit.Test

class ResourceTest {

    @Test
    fun `Success resource holds data`() {
        // Given: Some test data
        val testData = "Test Data"
        
        // When: Creating Success resource
        val resource = Resource.Success(testData)
        
        // Then: Data should be accessible
        assertEquals("Data should match", testData, resource.data)
        assertNull("Message should be null", resource.message)
    }

    @Test
    fun `Error resource holds message`() {
        // Given: An error message
        val errorMessage = "Something went wrong"
        
        // When: Creating Error resource
        val resource = Resource.Error<String>(errorMessage)
        
        // Then: Message should be accessible
        assertEquals("Message should match", errorMessage, resource.message)
        assertNull("Data should be null", resource.data)
    }

    @Test
    fun `Loading resource has no data or message`() {
        // When: Creating Loading resource
        val resource = Resource.Loading<String>()
        
        // Then: Both should be null
        assertNull("Data should be null", resource.data)
        assertNull("Message should be null", resource.message)
    }

    @Test
    fun `Error resource can hold partial data`() {
        // Given: Error with some cached data
        val cachedData = "Cached Data"
        val errorMessage = "Network error"
        
        // When: Creating Error with data
        val resource = Resource.Error(errorMessage, cachedData)
        
        // Then: Both should be accessible
        assertEquals("Data should match", cachedData, resource.data)
        assertEquals("Message should match", errorMessage, resource.message)
    }
}
