const db = require('./db')

const cartSchema = db.mongoose.Schema({
    user: {
        type: db.mongoose.Schema.Types.ObjectId,
        ref:'User',
        require:true
    },
    items:[
        {
            product:{
                type:db.mongoose.Schema.Types.ObjectId,
                ref:'Product',
                require:true,
            },
            quantity:{
                type:Number,
                default:1
            }
        }
    ]
}, { timestamps: true });

const Cart = db.mongoose.model('Cart', cartSchema);
module.exports = Cart
