import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import TestComponent from './Component1';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(<App />, document.getElementById('root'));
ReactDOM.render(<TestComponent name="a-new-component"/>, document.getElementById('component1'));
// DOM firstly render() App component via components tree (App.js)
// then App calls render() inside it
// the component 'App' is put in <div> component referenced by 'root' in index.html
registerServiceWorker();  // registerServiceWorker.js
