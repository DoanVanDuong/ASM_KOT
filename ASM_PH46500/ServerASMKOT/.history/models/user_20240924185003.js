const db = require('./db')
const userSchema = db.mongoose.Schema({
    name: {
        type: String,
        require: true
    },
    userName: {
        type: String,
        require: true,
        unique: true
    },
    email: {
        type: String,
        require: true,
        unique: true
    },
    password: {
        type: String,
        require: true,
    },

},{timestamps:true});


const User = db.mongoose.model('User', userSchema);
module.exports = User;