package com.udacity.heather.timetobake.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.databinding.StepListItemBinding;
import com.udacity.heather.timetobake.models.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private List<Step> steps;
    private final StepAdapterOnClickHandler mOnClickHandler;
    private int clickedItemPosition = -1;


    public StepAdapter(StepAdapterOnClickHandler mOnClickHandler) {
        this.mOnClickHandler = mOnClickHandler;
    }

    @NonNull
    @Override
    public StepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        StepListItemBinding stepBinding = StepListItemBinding.inflate(inflater, viewGroup, shouldAttachToParentImmediately);
        return new StepAdapterViewHolder(stepBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapterViewHolder stepAdapterViewHolder, int position) {
        stepAdapterViewHolder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }

    class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private StepListItemBinding stepBinding;

        StepAdapterViewHolder(StepListItemBinding binding) {
            super(binding.getRoot());
            stepBinding = binding;
            itemView.setOnClickListener(this);
        }

        void bindItem(int position) {
            Step currentStep = steps.get(position);
            String stepId = String.valueOf(currentStep.getId() + 1);
            stepBinding.tvStepId.setText(stepId);
            stepBinding.tvStepShortDescription.setText(currentStep.getShortDescription());

            if (clickedItemPosition == position) {
                stepBinding.stepItemContainer.setBackgroundResource(R.drawable.recipe_background_dark);
            } else {
                stepBinding.stepItemContainer.setBackgroundResource(R.drawable.recipe_background);
            }
            stepBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            clickedItemPosition = adapterPosition;
            mOnClickHandler.onClick(adapterPosition);
            notifyDataSetChanged();
        }
    }
    public interface StepAdapterOnClickHandler {
        void onClick(int position);
    }
    public void setStepData(List<Step> stepData) {
        steps = stepData;
        notifyDataSetChanged();
    }
}
