var path = require('path');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var webpack = require('webpack');
require("babel-polyfill");

config = {
    entry : ['babel-polyfill', './app/index.js'],
    output: {
        path: path.resolve(__dirname, '../WebContent'),
        filename: 'teams_bundle.js',
        publicPath: '/'
    },
    module: {
        loaders : [
            {
                test: /\.jsx?$/,
                exclude: /(node_modules|bower_components)/,
                loader: 'babel-loader',
                query: {
                    presets: ['react', 'env', 'stage-0'],
                    plugins: ['react-html-attrs', 'transform-class-properties', 'transform-decorators-legacy']
                }
            },
	    { test: /\.css$/, loader: "style-loader!css-loader" }
        ],
    },
    plugins: [],
    devServer: {
        historyApiFallback: true
    }
};

if (process.env.NODE_ENV === 'production') {
    config.plugins.push(
        new webpack.DefinePlugin({
            'process.env' : {
            'NODE_ENV' : JSON.stringify(process.env.NODE_ENV)
        }
        }),
        new webpack.optimize.UglifyJsPlugin(),
        new webpack.optimize.OccurrenceOrderPlugin()
    )
}

module.exports = config;
