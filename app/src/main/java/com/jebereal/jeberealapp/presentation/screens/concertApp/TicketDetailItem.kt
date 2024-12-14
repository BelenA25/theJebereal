package com.jebereal.jeberealapp.presentation.screens.concertApp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jebereal.jeberealapp.core.TicketConcert

@Composable
fun TicketDetailItem(
    transaction: Map<String, Any>,
    ticket: TicketConcert?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Ticket ID
            Text(
                text = "Ticket ID: ${transaction["ticketId"]}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Price
            Text(
                text = "Price: $${transaction["amount"]}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Seat Number
            Text(
                text = "Seat Number: ${ticket?.seatNumber ?: "Not available"}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Row
            Text(
                text = "Row: ${ticket?.row ?: "Not available"}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Type
            Text(
                text = "Type: ${ticket?.type ?: "Not available"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
