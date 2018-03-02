import React from 'react';
import {processConnection} from "../../../actions/commonActions";
import {withCookies} from 'react-cookie';
import {connect} from 'react-redux';


@connect((store)=>({
  authenticated: store.common.authenticated,
  redirect: store.common.loginRedirectUrl
}))
class UnknownUserForm extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      email:'',
      password: '',
      errorMessage: '',
      error: false
    };
    this.handleInput = this.handleInput.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
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
    });
  }

  componentWillMount(){
    console.log('UnknowUserForm/PROPS', this.props);
  }

  render() {
    return (
      <div>
        <h1>logo</h1>
        <h2>ACCESS YOUR ACCOUNT</h2>
        <form method="POST" onSubmit={this.onSubmit} id="unknownUserForm">
          <div>
            <input type="email" name="email" placeholder="Email"
                   value={this.state.email}
                   onChange={this.handleInput}
                   required/>
            <input type="password" name="password" placeholder="Password"
                   value={this.state.password}
                   onChange={this.handleInput}
                   required/>
          </div>
          <button type="submit">Login</button>
          <div>
            <p>Password lost ?</p>
          </div>
          <div>
            <p>{this.state.errorMessage}</p>
          </div>
          <div>
            <a href="/discover">Create an account</a>
          </div>
        </form>
      </div>
    )
  }
}

export default withCookies(UnknownUserForm);