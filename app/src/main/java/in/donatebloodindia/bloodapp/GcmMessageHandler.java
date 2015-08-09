package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

//import com.google.android.gms.gcm.GoogleCloudMessaging;

//@SuppressLint("NewApi")
//public class GcmMessageHandler extends IntentService
//{

   /* String mes;
    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();

        //GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("message");
        //showToast();
        Log.d("GCM", "Received : (" +messageType+")  "+extras.getString("message"));

        MyGcmBroadcastReciever.completeWakefulIntent(intent);


        //Create notification
        NotificationManager notifi_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Uri notificationsound_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent_notifi = new Intent( this, ActivityFirstLogin.class);
        PendingIntent pending_intent =  PendingIntent.getActivity(this, 0, intent_notifi, 0);
        Notification notifi = new Notification.Builder(this).setContentText(mes).setContentTitle("Blood Donation App")
                .setSmallIcon(R.drawable.bloodappicon)
                .setContentIntent(pending_intent)
                .setSound(notificationsound_uri)
                .setAutoCancel(true).build();

        notifi_manager.notify(0, notifi);

    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
            }
        });

    }*/

//}