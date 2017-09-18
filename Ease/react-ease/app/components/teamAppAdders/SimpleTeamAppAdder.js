import React, {Component} from "react";
import {dashboardAndTeamAppSearch} from "../../utils/api";
import {handleSemanticInput} from "../../utils/utils";
import {connect} from "react-redux";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

const AppResultRenderer = (props) => {
  return (
      <div></div>
  )
};

class SimpleTeamAppSearch extends Component {
  constructor(props){
    super(props);
    this.state = {
      apps: [],
      loading: true,
      value: ''
    }
  }
  handleInput = (e, {value}) => {
    this.setState({value: value});
  };
  componentDidMount(){
    console.log('starting');
    dashboardAndTeamAppSearch(this.props.team_id, '').then(response => {
      console.log(response);
      this.setState({apps: response, loading: false});
    });
  }
  render(){
    return (
        <Search
            fluid
            minCharacters={0}
            placeholder="Search websites here..."
            value={this.state.value}
            class="inverted"
            onSearchChange={this.handleInput}
            size="mini"
            results={this.state.apps}/>
    )
  }
}

@connect(store => ({
  team_id: store.team.id
}))
class SimpleTeamAppAdder extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <Container fluid id="simple_team_app_add">
          <SimpleTeamAppSearch team_id={this.props.team_id}/>

        </Container>
    )
  }
}

module.exports = SimpleTeamAppAdder;