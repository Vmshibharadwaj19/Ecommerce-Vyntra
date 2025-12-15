import { create } from 'zustand';

// Zustand store for global state management
export const useStore = create((set, get) => ({
  // Search state
  searchQuery: '',
  setSearchQuery: (query) => set({ searchQuery: query }),

  // Filter state
  filters: {
    categoryId: null,
    subCategoryId: null,
    minPrice: null,
    maxPrice: null,
    brand: null,
    rating: null,
    sortBy: 'popularity',
  },
  setFilters: (filters) => set((state) => ({ 
    filters: { ...state.filters, ...filters } 
  })),
  resetFilters: () => set({ 
    filters: {
      categoryId: null,
      subCategoryId: null,
      minPrice: null,
      maxPrice: null,
      brand: null,
      rating: null,
      sortBy: 'popularity',
    }
  }),

  // UI state
  sidebarOpen: false,
  setSidebarOpen: (open) => set({ sidebarOpen: open }),

  // Recently viewed products
  recentlyViewed: [],
  addToRecentlyViewed: (product) => set((state) => {
    const filtered = state.recentlyViewed.filter(p => p.id !== product.id);
    return { recentlyViewed: [product, ...filtered].slice(0, 10) };
  }),
}));

