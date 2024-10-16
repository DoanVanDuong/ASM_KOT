const db = require('./db')
const orderSchema = db.mongoose.Schema({
    user: {
        type: db.mongoose.Schema.Types.ObjectId,
        ref: 'User',
        require: true
    },
    items: [{
        product: {
            type: db.mongoose.Schema.Types.ObjectId,
            ref: 'Product',
            require: true
        },
        quantity: {
            type: Number,
            require: true,
        }
    }],
    totalAmount: {
        type: Number,
        require: true
    },
    paymentMethod:{
        type: String,
        require: true,
        default:"Thanh toán trực tiếp"
    },
    shippingAddress: {
        address: {
            type: String,
            require: true
        },
        phoneNumber: {
            type: String,
            require: true
        }
    },
    status: {
        type: String,
        enum: ['Pending', 'Shipped', 'Delivered'],
        default: 'Pending'
    }
}, { timestamps: true });

const Order = db.mongoose.model('Order', orderSchema);

module.exports = Order;