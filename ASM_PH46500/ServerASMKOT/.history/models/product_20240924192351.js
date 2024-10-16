const db = require('./db')

const productSchema = db.mongoose.Schema({
    name: {
        type: String,
        require: true
    },
    image_url: {
        type: String,
        require: true
    },
    price: {
        type: Number,
        require: true
    },
    description: {
        type: String,
        require: true
    },
    category: {
        type: String,
        require: true
    },
}, { timestamps: true });

const Product = db.mongoose.model('Product', productSchema);
module.exports = Product
