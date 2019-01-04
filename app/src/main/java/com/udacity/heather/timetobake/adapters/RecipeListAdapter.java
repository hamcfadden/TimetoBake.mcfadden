package com.udacity.heather.timetobake.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.heather.timetobake.databinding.RecipeListItemBinding;
import com.udacity.heather.timetobake.models.Recipe;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.LayoutInflater.from;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private List<Recipe> recipeList;
    private RecipeListAdapterOnClickHandler clickHandler;

    public RecipeListAdapter(RecipeListAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

        public void setRecipesData (List < Recipe > recipeList) {
            this.recipeList = recipeList;
            notifyDataSetChanged();
        }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = from(context);
        RecipeListItemBinding itemBinding = RecipeListItemBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        holder.bind(currentRecipe);
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) return 0;
        return recipeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RecipeListItemBinding itemBinding;


        ViewHolder(RecipeListItemBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
            itemView.setOnClickListener(this);
        }


        public void bind(Recipe recipe) {
            String recipeId = String.valueOf(recipe.getId());
            itemBinding.tvRecipeName.setText(recipe.getName());
            itemBinding.tvRecipeId.setText(recipeId);
            itemBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe currentRecipe = recipeList.get(adapterPosition);
            clickHandler.onClick(currentRecipe);
        }
    }
            public interface RecipeListAdapterOnClickHandler {
                void onClick(Recipe currentRecipe);
            }

    }

