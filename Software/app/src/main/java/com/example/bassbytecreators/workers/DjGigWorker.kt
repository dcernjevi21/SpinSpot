import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.helpers.RetrofitClient
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.withContext


class DJGigWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        // Dohvaćanje gaža
        val djGigs = fetchGigs(userId)

        if (djGigs.isEmpty()) {
            Log.e("WORKER_ERROR", "Nije pronađena nijedna gaža za userId: $userId")
            return Result.failure()
        }

        // Trenutni datum
        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Prolazi kroz sve gaže i provjerava preostale dane
        djGigs.forEach { gig ->
            val gigDate = Calendar.getInstance()
            gigDate.time = dateFormat.parse(gig.gigDate) ?: return@forEach

            // Izračun dana preostalih do gaže
            val daysLeft = ((gigDate.timeInMillis - currentDate.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

            when (daysLeft) {
                5 -> sendNotification("Gaza za 5 dana", "Gaža '${gig.name}' je zakazana za 5 dana!")
                3 -> sendNotification("Gaza za 3 dana", "Gaža '${gig.name}' je zakazana za 3 dana!")
                0 -> sendNotification("Gaža je danas!", "Gaža '${gig.name}' se održava danas!")
            }
        }
        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "dj_gig_notifications"

        // Provjera dozvole za notifikacije
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (applicationContext.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("NotificationError", "Dozvola za slanje notifikacija nije odobrena.")
                return
            }
        }

        // Kreiranje kanala za notifikacije (za Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "DJ Gig Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Kreiranje i slanje notifikacije
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(title.hashCode(), notification)
    }


    private suspend fun fetchGigs(userId: Int): List<DJGig> {
        return withContext(Dispatchers.IO) {
            try {
                RetrofitClient.apiService.getGigs(userId)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Greška kod dohvaćanja gaža: ${e.message}", e)
                emptyList()
            }
        }
    }
}
