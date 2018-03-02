import React from 'react';
import {connect} from "react-redux";
import {withCookies} from 'react-cookie';
import {processConnection} from "../../../actions/commonActions";

@connect((store)=>({
  authenticated: store.common.authenticated,
  redirect: store.common.loginRedirectUrl
}))
class KnownUserForm extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      name: this.props.cookies.get('fname'),
      email: this.props.cookies.get('email'),
      password: '',
      errorMessage: '',
      error: false
    };
    if (!!this.state.name) {
      console.log(this.state.name);
      this.state.name = atob(this.state.name);
    }
    this.onSubmit = this.onSubmit.bind(this);
    this.handleInput = this.handleInput.bind(this);
  }

  onSubmit(e){
    e.preventDefault();
    this.setState({error: false});
    this.props.dispatch(processConnection({
      email:this.state.email,
      password:this.state.password
    })).then(response => {
      this.props.finishLogin();
    }).catch(err => {
      this.setState({errorMessage:err, error: true, password: ''});
      this.props.setView('known');
    });
  }

  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }

  componentWillMount(){
    console.log(this.state.email);
    console.log('KnowUserForm/PROPS', this.props);
    if (!this.props.cookies.get('fname') || !this.props.cookies.get('email')) {
      this.props.history.replace('/login/unknownUser');
    }
}

  render() {
    return (
      <div>
        <div>
          <h1>Hello {this.state.name}</h1>
        </div>
        <form method="POST" onSubmit={this.onSubmit} id="knownUserForm">
          <div>
            <p>Please type your password to access your space</p>
          </div>
            <input type="password" name="password" placeholder="Password"
                   value={this.state.password}
                   onChange={this.handleInput}
                   autoFocus
                   required/>
            <p>Password lost ?</p>
            <button type="submit">Login</button>
            <p>Other account</p>
            <a>Create an account</a>
        </form>
      </div>

    )
  }
}

export default withCookies(KnownUserForm);