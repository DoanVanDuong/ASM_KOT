const db = require('./db');

const categorySchema = db.mongoose.Schema({
    name: {
        type: String,
        required: true
    }
}, { timestamps: true });

const Category = db.mongoose.model('Category', categorySchema);
module.exports = Category;
