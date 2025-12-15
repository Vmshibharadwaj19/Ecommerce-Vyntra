import React, { useState, useEffect } from 'react';
import * as categoryAPI from '../../api/category';
import './ManageCategories.css';

const ManageCategories = () => {
  const [categories, setCategories] = useState([]);
  const [subCategories, setSubCategories] = useState({});
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [editingCategory, setEditingCategory] = useState(null);
  const [editingSubCategory, setEditingSubCategory] = useState(null);
  const [selectedCategoryId, setSelectedCategoryId] = useState(null);
  const [showAddCategory, setShowAddCategory] = useState(false);
  const [showAddSubCategory, setShowAddSubCategory] = useState(false);
  
  // Form states
  const [categoryForm, setCategoryForm] = useState({ name: '', description: '', imageUrl: '' });
  const [subCategoryForm, setSubCategoryForm] = useState({ name: '', description: '', categoryId: '' });

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    if (selectedCategoryId) {
      fetchSubCategories(selectedCategoryId);
    }
  }, [selectedCategoryId]);

  const fetchCategories = async () => {
    try {
      const response = await categoryAPI.getAllCategories();
      setCategories(response.data.data || []);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching categories:', error);
      setMessage('Error fetching categories');
      setLoading(false);
    }
  };

  const fetchSubCategories = async (categoryId) => {
    try {
      const response = await categoryAPI.getSubCategories(categoryId);
      setSubCategories(prev => ({ ...prev, [categoryId]: response.data.data || [] }));
    } catch (error) {
      console.error('Error fetching subcategories:', error);
    }
  };

  const handleCreateCategory = async (e) => {
    e.preventDefault();
    try {
      await categoryAPI.createCategory(categoryForm);
      setMessage('Category created successfully!');
      setShowAddCategory(false);
      setCategoryForm({ name: '', description: '', imageUrl: '' });
      fetchCategories();
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Error creating category: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleUpdateCategory = async (e) => {
    e.preventDefault();
    try {
      await categoryAPI.updateCategory(editingCategory.id, categoryForm);
      setMessage('Category updated successfully!');
      setEditingCategory(null);
      setCategoryForm({ name: '', description: '', imageUrl: '' });
      fetchCategories();
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Error updating category: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleDeleteCategory = async (id) => {
    if (!window.confirm('Are you sure you want to delete this category? All subcategories will also be deleted.')) {
      return;
    }
    try {
      await categoryAPI.deleteCategory(id);
      setMessage('Category deleted successfully!');
      fetchCategories();
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Error deleting category: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleCreateSubCategory = async (e) => {
    e.preventDefault();
    try {
      await categoryAPI.createSubCategory({
        ...subCategoryForm,
        categoryId: parseInt(subCategoryForm.categoryId)
      });
      setMessage('SubCategory created successfully!');
      setShowAddSubCategory(false);
      setSubCategoryForm({ name: '', description: '', categoryId: '' });
      if (subCategoryForm.categoryId) {
        fetchSubCategories(parseInt(subCategoryForm.categoryId));
      }
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Error creating subcategory: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleUpdateSubCategory = async (e) => {
    e.preventDefault();
    try {
      await categoryAPI.updateSubCategory(editingSubCategory.id, {
        ...subCategoryForm,
        categoryId: parseInt(subCategoryForm.categoryId)
      });
      setMessage('SubCategory updated successfully!');
      setEditingSubCategory(null);
      setSubCategoryForm({ name: '', description: '', categoryId: '' });
      if (subCategoryForm.categoryId) {
        fetchSubCategories(parseInt(subCategoryForm.categoryId));
      }
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Error updating subcategory: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleDeleteSubCategory = async (id, categoryId) => {
    if (!window.confirm('Are you sure you want to delete this subcategory?')) {
      return;
    }
    try {
      await categoryAPI.deleteSubCategory(id);
      setMessage('SubCategory deleted successfully!');
      fetchSubCategories(categoryId);
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Error deleting subcategory: ' + (error.response?.data?.message || error.message));
    }
  };

  const startEditCategory = (category) => {
    setEditingCategory(category);
    setCategoryForm({
      name: category.name || '',
      description: category.description || '',
      imageUrl: category.imageUrl || ''
    });
    setShowAddCategory(true);
  };

  const startEditSubCategory = (subCategory, categoryId) => {
    setEditingSubCategory(subCategory);
    setSubCategoryForm({
      name: subCategory.name || '',
      description: subCategory.description || '',
      categoryId: categoryId.toString()
    });
    setShowAddSubCategory(true);
  };

  const cancelEdit = () => {
    setEditingCategory(null);
    setEditingSubCategory(null);
    setShowAddCategory(false);
    setShowAddSubCategory(false);
    setCategoryForm({ name: '', description: '', imageUrl: '' });
    setSubCategoryForm({ name: '', description: '', categoryId: '' });
  };

  if (loading) {
    return <div className="manage-categories-loading">Loading categories...</div>;
  }

  return (
    <div className="manage-categories">
      <div className="container">
        <div className="header-section">
          <h2>Manage Categories & SubCategories</h2>
          <div className="action-buttons">
            <button 
              onClick={() => {
                cancelEdit();
                setShowAddCategory(true);
              }}
              className="btn-add"
            >
              + Add Category
            </button>
            <button 
              onClick={() => {
                cancelEdit();
                setShowAddSubCategory(true);
              }}
              className="btn-add"
            >
              + Add SubCategory
            </button>
          </div>
        </div>

        {message && (
          <div className={`message ${message.includes('Error') ? 'error' : 'success'}`}>
            {message}
          </div>
        )}

        {/* Add/Edit Category Form */}
        {showAddCategory && (
          <div className="form-section">
            <h3>{editingCategory ? 'Edit Category' : 'Add New Category'}</h3>
            <form onSubmit={editingCategory ? handleUpdateCategory : handleCreateCategory}>
              <div className="form-group">
                <label>Category Name *</label>
                <input
                  type="text"
                  value={categoryForm.name}
                  onChange={(e) => setCategoryForm({ ...categoryForm, name: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Description</label>
                <textarea
                  value={categoryForm.description}
                  onChange={(e) => setCategoryForm({ ...categoryForm, description: e.target.value })}
                  rows="3"
                />
              </div>
              <div className="form-group">
                <label>Image URL</label>
                <input
                  type="url"
                  value={categoryForm.imageUrl}
                  onChange={(e) => setCategoryForm({ ...categoryForm, imageUrl: e.target.value })}
                  placeholder="https://example.com/image.jpg"
                />
              </div>
              <div className="form-actions">
                <button type="submit" className="btn-save">
                  {editingCategory ? 'Update' : 'Create'} Category
                </button>
                <button type="button" onClick={cancelEdit} className="btn-cancel">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Add/Edit SubCategory Form */}
        {showAddSubCategory && (
          <div className="form-section">
            <h3>{editingSubCategory ? 'Edit SubCategory' : 'Add New SubCategory'}</h3>
            <form onSubmit={editingSubCategory ? handleUpdateSubCategory : handleCreateSubCategory}>
              <div className="form-group">
                <label>Parent Category *</label>
                <select
                  value={subCategoryForm.categoryId}
                  onChange={(e) => setSubCategoryForm({ ...subCategoryForm, categoryId: e.target.value })}
                  required
                >
                  <option value="">Select Category</option>
                  {categories.map(cat => (
                    <option key={cat.id} value={cat.id}>{cat.name}</option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>SubCategory Name *</label>
                <input
                  type="text"
                  value={subCategoryForm.name}
                  onChange={(e) => setSubCategoryForm({ ...subCategoryForm, name: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Description</label>
                <textarea
                  value={subCategoryForm.description}
                  onChange={(e) => setSubCategoryForm({ ...subCategoryForm, description: e.target.value })}
                  rows="3"
                />
              </div>
              <div className="form-actions">
                <button type="submit" className="btn-save">
                  {editingSubCategory ? 'Update' : 'Create'} SubCategory
                </button>
                <button type="button" onClick={cancelEdit} className="btn-cancel">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Categories List */}
        <div className="categories-section">
          <h3>Categories ({categories.length})</h3>
          <div className="categories-grid">
            {categories.map(category => (
              <div key={category.id} className="category-card">
                <div className="category-header">
                  <h4>{category.name}</h4>
                  <div className="category-actions">
                    <button 
                      onClick={() => startEditCategory(category)}
                      className="btn-edit"
                    >
                      Edit
                    </button>
                    <button 
                      onClick={() => handleDeleteCategory(category.id)}
                      className="btn-delete"
                    >
                      Delete
                    </button>
                  </div>
                </div>
                {category.description && (
                  <p className="category-description">{category.description}</p>
                )}
                <button
                  onClick={() => setSelectedCategoryId(
                    selectedCategoryId === category.id ? null : category.id
                  )}
                  className="btn-view-subcategories"
                >
                  {selectedCategoryId === category.id ? 'Hide' : 'View'} SubCategories
                </button>
                
                {/* SubCategories for this category */}
                {selectedCategoryId === category.id && (
                  <div className="subcategories-list">
                    <h5>SubCategories</h5>
                    {subCategories[category.id]?.length > 0 ? (
                      subCategories[category.id].map(subCat => (
                        <div key={subCat.id} className="subcategory-item">
                          <span>{subCat.name}</span>
                          <div className="subcategory-actions">
                            <button 
                              onClick={() => startEditSubCategory(subCat, category.id)}
                              className="btn-edit-small"
                            >
                              Edit
                            </button>
                            <button 
                              onClick={() => handleDeleteSubCategory(subCat.id, category.id)}
                              className="btn-delete-small"
                            >
                              Delete
                            </button>
                          </div>
                        </div>
                      ))
                    ) : (
                      <p className="no-subcategories">No subcategories</p>
                    )}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManageCategories;
