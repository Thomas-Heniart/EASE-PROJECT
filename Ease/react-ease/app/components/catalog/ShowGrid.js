import React from 'react';
import { Grid, Image } from 'semantic-ui-react';
import { render } from 'react-router-dom';

class ShowGrid extends React.Component {

    constructor(props){
        super(props);
    }
    // showFourApps = fourApps => {
    //     console.log('showFourApps');
    //     return (fourApps.map((item, key) =>
    //         <Grid.Column key={key} className="showSegment">
    //             <Image src={item.logo}/>
    //             <p>{item.name}</p>
    //         </Grid.Column>
    //         )
    //     );
    //     // console.log('showFourAppsEnd');
    // };

    // appsRows = fourApps => {
    //     console.log('appsRows');
    //     return (
    //         <Grid.Row>
    //             {this.showFourApps(fourApps)}
    //         </Grid.Row>
    //     )
    // };

    // appsList = () => {
    //     console.log('appsList');
    //     let i = 0;
    //     let fourApps = [];
    //     let temp = this.state.apps;

    // const showFourApps = temp.map((item, key) =>
    //     <Grid.Column key={key} className="showSegment">
    //         <Image src={item.logo}/>
    //         <p>{item.name}</p>
    //     </Grid.Column>
    // );

    // this.state.apps.map((item, key) => {
    //     if (i < 4) {
    //         fourApps = [item].concat(fourApps);
    //         i++;
    //         console.log(fourApps);
    //     }
    //     if (i === 4) {
    //         i = 0;
    //         temp = fourApps;
    //         fourApps = [];
    //         console.log("Temp: ", temp, "FourApps: ", fourApps);
    //         return <Grid.Row key={key}>{showFourApps}</Grid.Row>
    //     }
    // });

    // };

    render() {
        let appsSorted = this.props.apps.filter((item) => {
            return item.category.toLowerCase().search(
                this.props.categorySelected.toLowerCase()) !== -1;
        });

        return (
            <Grid columns={4} className="logoCatalog">
                {appsSorted.map((item, key) =>
                    <Grid.Column key={key} className="showSegment">
                        <Image src={item.logo}/>
                        <p>{item.name}</p>
                    </Grid.Column>)
                }
            </Grid>
        )
    }
}

export default ShowGrid;