package com.udacity.heather.timetobake.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.heather.timetobake.databinding.RecipeListItemBinding;
import com.udacity.heather.timetobake.models.Recipe;

import java.util.List;

public class RecipeListAdapter
            extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    private RecipeListAdapterOnClickHandler clickHandler;

    public RecipeListAdapter(RecipeListAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }
    public void setRecipesData(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        this.context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        RecipeListItemBinding mainBinding = RecipeListItemBinding.inflate(inflater, viewGroup, shouldAttachToParentImmediately);
        return new ViewHolder(mainBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       int viewPosition = holder.getAdapterPosition();
        Recipe currentRecipe = recipeList.get(viewPosition);
        holder.bind(currentRecipe);
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) return 0;
        return recipeList.size();
    }
    public interface RecipeListAdapterOnClickHandler {
        void onClick(Recipe currentRecipe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       private final RecipeListItemBinding itemBinding;

      public ViewHolder(RecipeListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
           itemBinding.getRoot().setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            String recipeId = String.valueOf(recipe.getId());
            itemBinding.tvRecipeName.setText(recipe.getName());
            itemBinding.tvRecipeId.setText(recipeId);
            itemBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickHandler.onClick(recipeList.get(clickedPosition));
        }
    }
}