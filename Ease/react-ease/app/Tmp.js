import React, {Component} from "react";

class App extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div>

        </div>
    )
  }
}

class Profile extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div style={{height: '150px', marginBottom: '5px',backgroundColor: "#373B60"}}>

        </div>
    )
  }
}

class Column extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div class="display_flex flex_direction_column" style={{border: '1px solid black', height: '100%', width: '25%', backgroundColor: "#0A0"}}>
          <Profile/>
          <Profile/>
        </div>
    )
  }
}

class Tmp extends Component {
  constructor(props){
    super(props);

  }
  render(){
    return (
        <div class="display_flex" style={{width: '400px', height: '400px', padding:'2px', backgroundColor: '#A00'}}>
          <Column/>
          <Column/>
          <Column/>
          <Column/>
        </div>
    )
  }
}

export default Tmp;