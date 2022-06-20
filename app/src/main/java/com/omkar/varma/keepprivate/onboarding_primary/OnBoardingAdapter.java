package com.omkar.varma.keepprivate.onboarding_primary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omkar.varma.keepprivate.R;

import java.util.List;

public class OnBoardingAdapter extends  RecyclerView.Adapter<OnBoardingAdapter.onBoardingViewHolder>{

    private List<OnboardingItem> onboardingItems;

    public OnBoardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public onBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new onBoardingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_onboarding, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull onBoardingViewHolder holder, int position) {

        holder.setOnBoardingData(onboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class onBoardingViewHolder extends RecyclerView.ViewHolder{
        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageOnBoarding;

         onBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.titleTextOnBoarding);
            textDescription = itemView.findViewById(R.id.textDescriptionOnBoarding);
            imageOnBoarding = itemView.findViewById(R.id.imageOnBoarding);
        }

        void  setOnBoardingData(OnboardingItem onboardingItem){
            textTitle.setText(onboardingItem.getTitle());
            textDescription.setText(onboardingItem.getDescription());
            imageOnBoarding.setImageResource(onboardingItem.getImage());
        }
    }
}
