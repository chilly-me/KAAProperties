package com.example.kaaproperties.room.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.kaaproperties.logic.Events
import com.example.kaaproperties.room.entities.payments
import com.example.kaaproperties.room.entities.tenant

@Entity
data class tenantsWithPayments(
    @Embedded val tenants: tenant,
    @Relation(
        parentColumn = "tenantId",
        entityColumn = "tenantId"
    )
    val payments: List<payments>
)