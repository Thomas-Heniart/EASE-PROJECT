import React from 'react';
import ShowGrid from './ShowGrid';
import ListCategory from './ListCategory';
import RequestForm from './RequestForm';
import { Input, List, Button, Icon, Grid, Image, Segment, Checkbox, Form } from 'semantic-ui-react';
import { render } from 'react-router-dom';
import style from '../../../../WebContent/cssMinified.v00017/catalog.css';

class Catalog extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            categorySelected: '',
            searchInput: '',
            categories : [
                {name: 'Chill'},
                {name: 'Events'},
                {name: 'Finance'}
            ],
            apps: [
                {name: 'Instagram', logo: '/resources/websites/Instagram/logo.png', category: 'Chill'},
                {name: 'Spotify', logo: '/resources/websites/Spotify/logo.png', category: 'Events'},
                {name: 'Slack', logo: '/resources/websites/Slack/logo.png', category: 'Finance'},
                {name: '9gag', logo: '/resources/websites/9gag/logo.png', category: 'Chill'},
                {name: 'Outlook', logo: '/resources/websites/Outlook/logo.png', category: 'Finance'},
                {name: 'Youtube', logo: '/resources/websites/Youtube/logo.png', category: 'Events'},
                {name: 'Twitter', logo: '/resources/websites/Twitter/logo.png', category: 'Finance'},
                {name: 'Paypal', logo: '/resources/websites/Paypal/logo.png', category: 'Chill'},
                {name: 'Quora', logo: '/resources/websites/Quora/logo.png', category: 'Events'},
                {name: 'Blablacar', logo: '/resources/websites/BlaBlaCar/logo.png', category: 'Finance'},
                {name: 'Sens Critique', logo: '/resources/websites/SensCritique/logo.png', category: 'Chill'},
                {name: 'MailChimp', logo: '/resources/websites/Mailchimp/logo.png', category: 'Events'}
            ],
            allApps: [],
            loading: false
        };
    }

    componentWillMount() {
        this.setState({allApps: this.state.apps});
    }

    search = (e) => {
        let appsFiltered = this.state.apps.filter((item) => {
            return item.name.toString().toLowerCase().search(
                e.target.value.toString().toLowerCase().trim()) !== -1;
        });
        this.setState({ searchInput: e.target.value, allApps: appsFiltered });
    };

    sortList = (e, selected) => {
        let appsFiltered = this.state.apps.filter((item) => {
            return item.name.toString().toLowerCase().search('') !== -1;
        });
        this.setState({ searchInput: '', categorySelected: selected, allApps: appsFiltered });
    };

    showAllApps = (e) => {
        let appsFiltered = this.state.apps.filter((item) => {
            return item.name.toString().toLowerCase().search('') !== -1;
        });
        this.setState({ searchInput: '', categorySelected: '', allApps: appsFiltered });
    };

    render() {

        let appsSorted = this.state.allApps.filter((item) => {
            return item.category.toLowerCase().search(
                this.state.categorySelected.toLowerCase()) !== -1;
        });

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
                            <Button className="bookmarkButton">
                                <Icon name="bookmark" />
                                Add a Bookmark
                            </Button>
                            <Button className="importButton" color="facebook">
                                <Icon name="facebook" />
                                Import Accounts
                            </Button>
                            <ListCategory categories={this.state.categories} sortList={this.sortList} showAllApps={this.showAllApps} />
                        </Grid.Column>
                        <Grid.Column width={10}>
                            {appsSorted.length ?
                                <div>
                                    <h3>{this.state.categorySelected}</h3>
                                    <ShowGrid apps={this.state.allApps} categorySelected={this.state.categorySelected} />
                                </div>
                                :
                                <div/>
                            }
                            {this.state.searchInput !== '' && this.state.categorySelected !== '' && this.state.allApps.length ?
                                <div>
                                    <h3>Others</h3>
                                    <ShowGrid apps={this.state.allApps} categorySelected='' />
                                </div>
                                :
                                <div/>
                            }
                            {!this.state.allApps.length ?
                                <div>
                                    <h3>Cannot find your App?</h3>
                                    <RequestForm loading={this.state.loading} />
                                </div>
                                :
                                <div/>
                            }
                        </Grid.Column>
                        <Grid.Column width={3} />
                    </Grid>
                </div>
            </div>
        )
    }
}

export default Catalog;