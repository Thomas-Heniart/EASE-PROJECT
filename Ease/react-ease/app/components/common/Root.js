import React, {Component} from "react";
import {withCookies} from 'react-cookie';
import {connect} from "react-redux";

@connect((store)=>({
  authenticated: store.common.authenticated
}))
class Root extends Component{
  constructor(props){
    super(props);
  }
  componentWillMount(){
    if (this.props.authenticated) {
      this.props.history.replace('/main/dashboard');
      return;
    }
    const skip = localStorage.getItem('skipLanding');
    if (!!skip)
      this.props.history.replace('/login');
    else
      window.location.href = '/discover';
  }
  render(){
    return null;
  }
}

export default Root;