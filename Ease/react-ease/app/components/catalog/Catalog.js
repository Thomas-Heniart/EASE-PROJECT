import React from 'react';
import ShowGrid from './ShowGrid';
import ListCategory from './ListCategory';
import RequestForm from './RequestForm';
import AddBookmark from './AddBookmark';
import { Input, List, Button, Icon, Grid, Image, Segment, Checkbox, Form } from 'semantic-ui-react';
import { render } from 'react-router-dom';
import style from '../../../../WebContent/cssMinified.v00017/catalog.css';
import getWebsitesCatalog from '../../utils/api';
import api from '../../utils/api';

class Catalog extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      categorySelected: '',
      searchInput: '',
      categories: [
        {name: 'Chill'},
        {name: 'Events'},
        {name: 'Finance'}
      ],
      apps: [],
      allApps: [],
      loading: false,
      bookmark: false,
    }
  }

  updateApps = () => {
    api.getWebsitesCatalog().then((data) => {
      let appsSorted = data.websites.sort(function (a, b) {
        return b.integration_date - a.integration_date;
      });
      this.setState({apps: appsSorted, allApps: appsSorted})
    });
    // api.getCategories().then((data) => {
    //     this.setState({categories: data.categories})
    // });
  };

  componentDidMount() {
    this.updateApps();
  }

  search = (e) => {
    let appsFiltered = this.state.apps.filter((item) => {
      return item.name.toString().toLowerCase().search(
          e.target.value.toString().toLowerCase().trim()) !== -1;
    });
    this.setState({ searchInput: e.target.value, allApps: appsFiltered, bookmark: false });
  };

  sortList = (e, selected) => {
    let appsFiltered = this.state.apps.filter((item) => {
      return item.name.toString().toLowerCase().search('') !== -1;
    });
    this.setState({ searchInput: '', categorySelected: selected, allApps: appsFiltered, bookmark: false });
  };

  showAllApps = (e) => {
    let appsFiltered = this.state.apps.filter((item) => {
      return item.name.toString().toLowerCase().search('') !== -1;
    });
    this.setState({ searchInput: '', categorySelected: '', allApps: appsFiltered, bookmark: false });
  };

  bookmarkActive = () => {
    this.setState({ bookmark: true, searchInput: '', categorySelected: '' });
  };

  render() {

    // let appsSorted = this.state.allApps.filter((item) => {
    //     return item.category.toLowerCase().search(
    //         this.state.categorySelected.toLowerCase()) !== -1;
    // });

    return (
        <div id="catalog">
            <header>
                <div className="container">
                    <div>
                        <p>Catalogue d'Apps</p>
                        <Input
                            className="inputSearch centered"
                            placeholder='Search'
                            onChange={e => this.search(e)}
                            value={this.state.searchInput} />
                    </div>
                </div>
            </header>
            <div className="container">
                <Grid>
                    <Grid.Column width={3}>
                        <Button className="bookmarkButton" onClick={this.bookmarkActive}>
                            <Icon name="bookmark" />
                            Add a Bookmark
                        </Button>
                        <Button className="importButton" color="facebook">
                            <Icon name="facebook" />
                            Import Accounts
                        </Button>
                        <ListCategory categories={this.state.categories} sortList={this.sortList} showAllApps={this.showAllApps} categorySelected={this.state.categorySelected}/>
                    </Grid.Column>
                    <Grid.Column width={10}>
                      {!this.state.bookmark &&
                      <div>
                          <h3>{this.state.categorySelected}</h3>
                          <ShowGrid apps={this.state.allApps} categorySelected={this.state.categorySelected} />
                      </div>}
                      {this.state.searchInput !== '' && this.state.categorySelected !== '' && this.state.allApps.length && !this.state.bookmark &&
                      <div>
                          <h3>Others</h3>
                          <ShowGrid apps={this.state.allApps} categorySelected='' />
                      </div>}
                      {!this.state.allApps.length && !this.state.bookmark &&
                      <div>
                          <h3>Cannot find your App?</h3>
                          <RequestForm loading={this.state.loading} />
                      </div>}
                      {this.state.bookmark &
                      <AddBookmark />}
                    </Grid.Column>
                    <Grid.Column width={3} />
                </Grid>
            </div>
        </div>
    )
  }
}

export default Catalog;