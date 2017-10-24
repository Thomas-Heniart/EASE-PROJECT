import React from 'react';
import ShowGrid from './ShowGrid';
import ListCategory from './ListCategory';
import Categories from "./Categories";
import WebsitesContainer from "./WebsitesContainer";
import RequestForm from './RequestForm';
import AddBookmark from './AddBookmark';
import {handleSemanticInput} from "../../utils/utils";
import { Sticky, Rail, Input, List, Button, Icon, Grid, Image, Segment, Checkbox, Form } from 'semantic-ui-react';
import style from '../../../../WebContent/cssMinified.v00017/catalog.css';
import getWebsitesCatalog from '../../utils/api';
import api from '../../utils/api';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import { NavLink } from 'react-router-dom';
import { Switch, Route } from 'react-router-dom';
import {requestWebsite} from "../../actions/teamModalActions";

@connect(store => ({
  catalog: store.catalog
}), reduxActionBinder)
class Catalog extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      query: ''
    }
  }
  handleInput = handleSemanticInput.bind(this);
  componentDidMount() {
    if (!this.props.catalog.loaded)
      this.props.fetchCatalog();
  }
  render() {
    return (
        <div id="catalog">
          <header>
            <div className="container">
              <div>
                <p>Catalogue d'Apps</p>
                <Input
                    className="inputSearch centered"
                    placeholder='Search'
                    name="query"
                    onChange={this.handleInput}
                    value={this.state.query} />
              </div>
            </div>
          </header>
          <div className="container">
            <Grid>
              <Grid.Column width={3}>
                <div id="catalog-nav">
                  <Button as={NavLink} to={`/main/catalog/bookmark`} className="bookmarkButton">
                    <Icon name="bookmark" />
                    Add a Bookmark
                  </Button>
                  <Button className="importButton" color="facebook">
                    <Icon name="facebook" />
                    Import Accounts
                  </Button>
                  <Categories/>
                </div>
              </Grid.Column>
              <Grid.Column width={10} style={{marginTop: '23px'}}>
                <Switch>
                  <Route path={`${this.props.match.path}/bookmark`} component={AddBookmark}/>
                  <Route path={`${this.props.match.path}`} render={(props) => <WebsitesContainer {...props} query={this.state.query}/>}/>
                </Switch>
              </Grid.Column>
              <Grid.Column width={3} />
            </Grid>
          </div>
        </div>
    )
  }
}

export default Catalog;