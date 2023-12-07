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

public class FavoriteLocationsAdapter extends RecyclerView.Adapter<FavoriteLocationsAdapter.ViewHolder> {

    private List<LocationItem> locations;
    private LayoutInflater inflater;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

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

    public LocationItem getItem(int position) {
        return locations.get(position);
    }

    public void setLocations(List<LocationItem> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textViewLocation;

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

