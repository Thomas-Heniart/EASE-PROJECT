import React, {Component} from 'react';
import { Container, Loader } from 'semantic-ui-react';
import {handleSemanticInput} from "../../utils/utils";
import AppsContainer from "./AppsContainer";
import AddAnyApp from './AddAnyApp';
import {connect} from "react-redux";
import { Switch, Route } from 'react-router-dom';
import CategoryAppsContainer from "./CategoryAppsContainer";
import {reduxActionBinder} from "../../actions/index";

@connect(store => ({
  catalog: store.catalog
}), reduxActionBinder)
class WebsitesContainer extends Component{
  constructor(props){
    super(props);
    this.state = {
      query: ''
    }
  }
  handleInput = handleSemanticInput.bind(this);
  openModal = (item) => {
    if (!item.sso_id) {
      this.props.showCatalogAddAppModal({
        active: true,
        website: item
      });
    }
    else {
      this.props.showCatalogAddSSOAppModal({
        active: true,
        website: item
      });
    }
  };
  render(){
    const query = this.props.query;
    const queryRegexp = new RegExp(query.toLowerCase()
      .replace(/\s+/g, '')
      .replace(/\\/g, '\\\\')
      .replace(/\?/g, '\\?')
      .replace(/\*/g, '\\*')
      .replace(/\[/g, '\\[')
      .replace(/]/g, '\\]')
      .replace(/\(/g, '\\(')
      .replace(/\)/g, '\\)')
      .replace(/\+/g, '\\+'));
    let websites = this.props.catalog.websites;
    if (query.length > 0)
      websites = websites.filter(item => {
        return item.name.toLowerCase().replace(/\s+/g, '').match(queryRegexp) !== null
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
                   render={(props) => <AppsContainer {...props} title={'Most popular Apps'} websites={websites} updates={query.length > 0 ? [] : this.props.catalog.updates} openModal={this.openModal}/>}/>
            <Route path={`${this.props.match.path}/:categoryId`}
                   render={(props) => <CategoryAppsContainer {...props} websites={websites} updates={query.length > 0 ? [] : this.props.catalog.updates} openModal={this.openModal}/>}/>
          </Switch>}
          {websites.length === 0 && this.props.catalog.loaded &&
            <AddAnyApp resetQuery={this.props.resetQuery} query={query} focus={true}/>}
        </Container>
    )
  }
}

export default WebsitesContainer;