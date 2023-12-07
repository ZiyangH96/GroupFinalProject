package algonquin.cst2355.groupfinalproject.SunriseSunset;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2355.groupfinalproject.SunriseSunset.LocationItem;
import algonquin.cst2355.groupfinalproject.R;


/**
 * The {@code FavoriteLocationsAdapter} class is responsible for managing the data and creating views
 * for the RecyclerView displaying favorite locations.
 */
public class FavoriteLocationsAdapter extends RecyclerView.Adapter<FavoriteLocationsAdapter.ViewHolder> {

    private List<LocationItem> locations;
    private LayoutInflater inflater;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    /**
     * Constructs a new {@code FavoriteLocationsAdapter} instance.
     *
     * @param context   The context.
     * @param locations The list of favorite locations.
     */
    public FavoriteLocationsAdapter(Context context, List<LocationItem> locations) {
        this.inflater = LayoutInflater.from(context);
        this.locations = locations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_favorite_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationItem location = locations.get(position);

        // Format the location information using string resources
        String locationInfo = holder.itemView.getContext().getString(R.string.location_info_format, location.getLatitude(), location.getLongitude());
        holder.textViewLocation.setText(locationInfo);
    }


    @Override
    public int getItemCount() {
        return locations.size();
    }

    /**
     * Gets the item at the specified position.
     *
     * @param position The position of the item.
     * @return The LocationItem at the specified position.
     */
    public LocationItem getItem(int position) {
        return locations.get(position);
    }

    /**
     * Sets the list of favorite locations and notifies the adapter of the data change.
     *
     * @param locations The new list of favorite locations.
     */
    public void setLocations(List<LocationItem> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    /**
     * Sets the item click listener for the RecyclerView.
     *
     * @param listener The item click listener.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * Sets the item long click listener for the RecyclerView.
     *
     * @param listener The item long click listener.
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when an item in the RecyclerView is clicked.
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Interface definition for a callback to be invoked when an item in the RecyclerView is long-clicked.
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    /**
     * The {@code ViewHolder} class represents each item view in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textViewLocation;

        /**
         * Constructs a new ViewHolder instance.
         *
         * @param itemView The item view.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
            return false;
        }
    }
}

