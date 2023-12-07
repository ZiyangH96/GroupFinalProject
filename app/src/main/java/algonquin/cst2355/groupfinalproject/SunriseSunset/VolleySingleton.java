package algonquin.cst2355.groupfinalproject.SunriseSunset;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * The {@code VolleySingleton} class provides a singleton instance of the Volley RequestQueue.
 * It ensures that there is only one RequestQueue for the entire application.
 */
public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    /**
     * Private constructor to create a new instance of the VolleySingleton.
     *
     * @param context The application context.
     */
    private VolleySingleton(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    /**
     * Gets the singleton instance of VolleySingleton.
     *
     * @param context The application context.
     * @return The singleton instance of VolleySingleton.
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    /**
     * Gets the singleton instance of the RequestQueue.
     *
     * @return The singleton instance of the RequestQueue.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds a request to the RequestQueue.
     *
     * @param req The request to be added.
     * @param <T> The type of the request.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

