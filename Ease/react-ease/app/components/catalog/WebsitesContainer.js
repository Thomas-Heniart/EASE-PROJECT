import React, {Component} from 'react';
import { Grid, Image, Icon, Modal, Header, Input, Container, Loader } from 'semantic-ui-react';
import {selectItemFromListById} from "../../utils/helperFunctions";
import AppsContainer from "./AppsContainer";
import RequestForm from './RequestForm';
import {connect} from "react-redux";
import { Switch, Route } from 'react-router-dom';
import CategoryAppsContainer from "./CategoryAppsContainer";

@connect(store => ({
  catalog: store.catalog
}))
class WebsitesContainer extends Component{
  constructor(props){
    super(props);
    this.state = {
      websites: []
    }
  }
  componentDidMount(){

  }
  render(){
    const query = this.props.query;
    let websites = this.props.catalog.websites;
    if (query.length > 0)
      websites = websites.filter(item => {
        return item.name.toLowerCase().replace(/\s+/g, '').match(query.toLowerCase()) !== null
      });
    return (
        <Container fluid>
          {this.props.catalog.fetching &&
          <h3>
            <Loader active inline='centered' />
          </h3>}
          {websites.length > 0 && !this.props.catalog.fetching &&
          <Switch>
            <Route exact
                   path={`${this.props.match.path}`}
                   render={(props) => <AppsContainer {...props} title={'Recently added'} websites={websites}/>}/>
            <Route path={`${this.props.match.path}/:categoryId`}
                   render={(props) => <CategoryAppsContainer {...props} websites={websites}/>}/>
          </Switch>}
          {websites.length === 0 && this.props.catalog.loaded &&
          <div>
            <h3>Cannot find your App?</h3>
            <RequestForm />
          </div>}
        </Container>
    )
  }
}

export default WebsitesContainer;